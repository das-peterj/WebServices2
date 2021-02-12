package com.iths;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Date;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


public class Server {

    public static void main(String[] args) throws IOException {

        ServerSocket socket = null;

        try {
            socket = new ServerSocket(8080);
        } catch (IOException e) {
            System.err.println("Could not start server: " + e);
            System.exit(-1);
        }
        System.out.println("FileServer accepting connections on port " + socket.getLocalPort());

        while (true) {

            Socket connection = null;
            try {
                // wait for request
                connection = socket.accept();
                BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                OutputStream out = new BufferedOutputStream(connection.getOutputStream());
                PrintStream pout = new PrintStream(out);

                String request = in.readLine();
                if (request == null)
                    continue;
                log(connection, request);
                while (true) {
                    String misc = in.readLine();
                    if (misc == null || misc.length() == 0)
                        break;


                }
                //String reqUrl = request.substring(4, request.length() - 9).trim();
                String reqType = request.split(" ")[0];
                String reqUrl = request.split(" ")[1];

                String path = "core/web" + reqUrl;
                File f = new File(path);

                if  (reqUrl.startsWith("/action_page")) {
                    String reqUrl1 = reqUrl.split("\\?")[1];
                    System.out.println(reqUrl1);
                    String reqUrlFirstName = reqUrl1.split("&")[0];
                    String reqUrlLastName = reqUrl1.split("&")[1];
                    System.out.println(reqUrlFirstName);
                    System.out.println(reqUrlLastName);

                } else if (f.isDirectory()) {
                    // if f file is inside the directory, add index.html
                    path = path + "index.html";
                    f = new File(path);
                }

                try {
                    // send the file
                    InputStream files = new FileInputStream(f);
                    File file = new File(path);
                    byte[] page = readFromFile(file);

                    pout.print("HTTP/1.1 200 OK\r\n" +
                            "Content-Type: " + guessContentType(path) + "\r\n" +
                            "Content-Length: " + page.length + "\r\n" +
                            "Date: " + new Date() + "\r\n" +
                            "Server: FileServer 1.0\r\n\r\n");
                    sendFile(files, out); // send raw file
                    log(connection, "200 OK");
                } catch (FileNotFoundException e) {
                    // file not found
                    errorReport(pout, connection, "404", "Not Found",
                            "The requested URL was not found on this server.");
                }
                out.flush();


            } catch (IOException e) {
                System.err.println(e);
            }
            try {
                if (connection != null) connection.close();
            } catch (IOException e) {
                System.err.println(e);
            }
        }
    }

    public static byte[] readFromFile(File file) {
        byte[] content = new byte[0];
        System.out.println("Does file exists: " + file.exists());
        if (file.exists() && file.canRead()) {
            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                content = new byte[(int) file.length()];
                int count = fileInputStream.read(content);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return content;
    }

    private static void errorReport(PrintStream pout, Socket connection,
                                    String code, String title, String msg) {
        pout.print("HTTP/1.1 " + code + " " + title + "\r\n" +
                "\r\n" +
                "<!DOCTYPE HTML PUBLIC \"-//IETF//DTD HTML 2.0//EN\">\r\n" +
                "<TITLE>" + code + " " + title + "</TITLE>\r\n" +
                "</HEAD><BODY>\r\n" +
                "<H1>" + title + "</H1>\r\n" + msg + "<P>\r\n" +
                "<HR><ADDRESS>FileServer 1.0 at " +
                connection.getLocalAddress().getHostName() +
                " Port " + connection.getLocalPort() + "</ADDRESS>\r\n" +
                "</BODY></HTML>\r\n");
        log(connection, code + " " + title);
    }

    private static String guessContentType(String path) {
        if (path.endsWith(".html") || path.endsWith(".htm"))
            return "text/html";
        else if (path.endsWith(".txt") || path.endsWith(".java"))
            return "text/plain";
        else if (path.endsWith(".gif"))
            return "image/gif";
        else if (path.endsWith(".pdf"))
            return "application/pdf";
        else if (path.endsWith(".class"))
            return "application/octet-stream";
        else if (path.endsWith(".jpg") || path.endsWith(".jpeg"))
            return "image/jpeg";
        else
            return "text/plain";
    }

    private static void log(Socket connection, String msg) {

        System.err.println(new Date() + " [" + connection.getInetAddress().getHostAddress() +
                ":" + connection.getPort() + "] " + msg);
    }

    private static void sendFile(InputStream file, OutputStream out) throws IOException {
        try {
            byte[] buffer = new byte[1000];
            while (file.available() > 0)
                out.write(buffer, 0, file.read(buffer));
        } catch (IOException e) {
            System.err.println(e);
        }
    }
}