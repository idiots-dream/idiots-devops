$(function () {
    const Xterm = {
        _term: null,
        init() {
            this._term = new Terminal({
                cursorBlink: true,
                cursorStyle: "block",
                scrollback: 1200,
                screenKeys: true,
                cursor: 'help',
                rows: 49,
                cols: 150
            })

            this._term.open(document.getElementById("terminal"), true)
            this._consoleBase()

            /* this._term.on('data', data => {
                this._term.write(data)
            }) */

            return this._term
        },

        reset() {
            this._term.reset()
            this._consoleBase()
        },

        _consoleBase() {
            this._term.writeln('Welcome to WebSSH')
            this._term.writeln('Author: neo-w7')
            console.log(new Date().toLocaleString())
            this._term.writeln(new Date().toLocaleString())
            this._term.writeln('Demo Version: 1.0.5')
            this._term.write('You can click on the connect button to connect to the terminal...')
        }
    }

    const Socket = {
        _socket: null,
        // 创建连接
        create() {
            if (this._socket) {
                this.close()
            }

            let protocol = window.location.protocol
            if (protocol.endsWith('http:')) {
                protocol = 'ws://'
            } else {
                protocol = 'wss://'
            }

            this._socket = new WebSocket(protocol + window.location.host + '/ssh')
            // this._socket = new WebSocket(protocol + window.location.hostname + ':8000/ssh')

            return this._socket
        },
        // 关闭连接
        close() {
            if (this.is()) {
                // 关闭当前链接
                this._socket.close()
                this._socket = null
            }
        },
        // 发送消息
        send(data) {
            console.log("websocket发送的消息：" + JSON.stringify(data))
            this._socket.send(JSON.stringify(data))
        },

        // 是否为空
        is() {
            return this._socket !== null
        }
    }
    const term = Xterm.init()
    term.on('data', data => {
        if (!Socket.is()) {
            return
        }

        Socket.send({
            type: 'command',
            message: data
        })
    })

    document.getElementById('connectBtn').onclick = () => {
        const
            host = document.getElementById('host').value,
            port = document.getElementById('port').value,
            username = document.getElementById('username').value,
            password = document.getElementById('password').value

        if (!host) {
            alert('请输入主机地址')
            return
        }

        if (!port) {
            alert('请输入端口号')
            return
        }

        if (!username) {
            alert('请输入用户名')
            return
        }

        if (!password) {
            alert('请输入密码')
            return
        }

        term.reset()
        // 关闭连接
        // Socket.close()

        const socket = Socket.create()

        // 指定连接成功后的回调函数
        socket.onopen = () => {
            term.write(`Connecting to ${host}:${port}...`)
            // 发送主机用户信息
            Socket.send({
                type: 'connect',
                message: { host, port, username, password }
            })
        }

        // 指定当从服务器接受到信息时的回调函数
        socket.onmessage = e => {
            if (e.data.endsWith("Connection failed.")) {
                Socket.close()
            }

            // 解决添加了cols属性后，文本超出时没有折行并将文本覆盖原有内容的bug
            if (e.data.length === 3) {
                term.write(e.data.trim())
            } else {
                term.write(e.data)
            }

            term.focus()
        }
    }
})
