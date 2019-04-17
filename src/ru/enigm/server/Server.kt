package ru.enigm.server

import java.io.*
import java.lang.Exception
import java.net.Socket
import java.util.*

// port to listen connection
val PORT = 33222

class Server(var connect: Socket) : Runnable {

    val WEB_ROOT = File("resources")
    val DEFAULT_FILE = "index.html"
    val FILE_NOT_FOUND = "404.html"
    val METHOD_NOT_SUPPORTED = "not_supported.html"

    override fun run() {
        //we manage our particular client connection
        var input: BufferedReader? = null
        var output: PrintWriter? = null
        var dataOutput: OutputStreamWriter? = null

        try {
            // we read characters from the client via input stream on socket
            input = BufferedReader(InputStreamReader(connect.getInputStream()))
            // we get character output stream to client (for headers)
            output = PrintWriter(connect.getOutputStream())
            // get binary output stream to client (for requested data)
            dataOutput = OutputStreamWriter(connect.getOutputStream())

            //get first line of the request from the client
            val inputString = input.readLine()
            // we parse the request with a string tokenizer
            val parse = StringTokenizer(inputString)
            val method = parse.nextToken().toUpperCase()

            // we support only GET and HEAD methods, we check
            if (method != "GET"){
                println("501 Not implemented method : $method ")

                // we send HTTP headers with data to client
                output.println("HTTP/1.1 501 Not Implemented")
                output.println("Server: Kotlin HTTP Server 1.0")
                output.println("Date: ${Date()}")
                output.println("Content-Type: json/application")
                output.println() // blank line between headers and content
                output.flush() // flush character output stream buffer

                //output body
                dataOutput.write("not implemented error\n")
                dataOutput.flush()
            }
        } catch (ioException: IOException) {
            println("Server error: $ioException")
        } finally {
            try {
                input?.close()
                output?.close()
                dataOutput?.close()
                connect.close()
            } catch (e: Exception) {
                println("Error closing stream : ${e.message}")
            }
            println("Connection closed.")
        }
    }
}