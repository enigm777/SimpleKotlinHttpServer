package ru.enigm.server.front

import ru.enigm.server.front.controller.PORT
import ru.enigm.server.front.controller.Server
import java.io.IOException
import java.net.ServerSocket
import java.util.*

fun main() {
    try {
        val serverSocket = ServerSocket(PORT)
        println("Server started.\nListening for connections on port : $PORT ...\n")

        while (true) {
            // блокирующее ожидание соединения на данном порту
            val clientSocket = serverSocket.accept()

            var myServer = Server(clientSocket)
            println("Connection opened. ( ${Date()} )")


            // create dedicated thread to manage the client connection
            var thread = Thread(myServer)
            thread.start()
        }
    } catch (e: IOException) {
        println("Server connection error : ${e.message}")
    }
}