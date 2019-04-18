package ru.enigm.server.front.controller

import java.io.*
import java.lang.Exception
import java.net.Socket
import java.util.*

// port to listen connection
val PORT = 33222

/**
 * Class implementing http server functions
 *
 * @author Ural Nigmatullin
 */
class Server(var connect: Socket) : Runnable {

    val MAIN_SCREEN_ARTICLES_PATH = "/skss/articles/main"
    val ADDITIONAL_SCREENS_ARTICLES_PATH = "/skss/articles/additional"

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
            // get  output stream to client (for requested front)
            dataOutput = OutputStreamWriter(connect.getOutputStream())

            //get first line of the request from the client
            val inputString = input.readLine()
            println("Request accepted = $inputString")
            // we parse the request with a string tokenizer
            val parse = StringTokenizer(inputString)
            val method = parse.nextToken().toUpperCase()

            // we support only GET methods
            if (method != "GET"){
                println("501 Not implemented method : $method ")

                // we send HTTP headers with front to client
                printHeaders(output,"HTTP/1.1 501 Not Implemented")

                //output body
                dataOutput.write("not implemented error\n")
                dataOutput.flush()
            } else {
                val path = parse.nextToken().toLowerCase()
                println(path)


                when(path) {
                    MAIN_SCREEN_ARTICLES_PATH -> printArticles("main")
                    ADDITIONAL_SCREENS_ARTICLES_PATH -> printArticles("additional")
                }

                printHeaders(output, "HTTP/1.1 200 OK")

                dataOutput.write("successful request\n")
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

    private fun printHeaders(output: PrintWriter, answer: String) {
        output.println(answer)
        output.println("Server: Kotlin HTTP Server 1.0")
        output.println("Date: ${Date()}")
        output.println("Content-Type: json/application")
        output.println() // blank line between headers and content
        output.flush() // flush character output stream buffer
    }

    private fun printArticles(screenName: String) {

    }
}