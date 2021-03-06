package com.iths;

import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Server {
    public static void main(String[] args) {

        ExecutorService execserv = Executors.newCachedThreadPool();
        try {
            ServerSocket serverSocket = new ServerSocket(8080);
            System.out.println(Thread.currentThread());
            System.out.println("FileServer accepting connections on port " + serverSocket.getLocalPort());

            while (true) {
                Socket socket = serverSocket.accept();
                execserv.execute(() -> handleConnection(socket));

            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void handleConnection(Socket socket) {

        System.out.println(Thread.currentThread());
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(socket.getInputStream());
            OutputStream out = new BufferedOutputStream(socket.getOutputStream());
            PrintStream pout = new PrintStream(out);

            String request = readLine(bufferedInputStream);
            String reqType = request.split(" ")[0];
            String reqUrl = request.split(" ")[1];

            if (reqType.startsWith("POST")) {
                request = handlePostRequest(bufferedInputStream);
            }

            String path = "";
            File f = new File(path);
            log(socket, request);

            if (reqUrl.startsWith("/jsonHandler")) {
                JavaSQL.getJSON();

                path = "core/web/jsonHandler.html";
                f = new File(path);
                File file = new File(path);
                byte[] page = readFromFile(file);

                InputStream files = new FileInputStream(f);

                pout.print("HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + guessContentType(path) + "\r\n" +
                        "Content-Length: " + page.length + "\r\n" +
                        "Date: " + new Date() + "\r\n" +
                        "Server: FileServer 1.0\r\n\r\n");
                pout.flush();
                sendFile(files, out);
                log(socket, "200 OK");
            } else {
                path = "core/web" + reqUrl;
                f = new File(path);
            }

            if (reqUrl.startsWith("/action_page.html")) {
                getInsertHandler(reqUrl);
            } else if (reqUrl.startsWith("/layout.css")) {
                path = "core/web/layout.css";
                f = new File(path);
            } else if (reqUrl.startsWith("/script.js")) {
                path = "core/web/script.js";
                f = new File(path);
            } else if (reqUrl.startsWith("/clockscript.js")) {
                path = "core/web/clockscript.js";
                f = new File(path);
            } else if (f.isDirectory()) {
                // om f path är inne i directory(core/web) så läggs index.html till i slutet
                path = "core/web/index.html";
                f = new File(path);
            }
            try {
                // skickar filen
                InputStream files = new FileInputStream(f);
                File file = new File(path);
                byte[] page = readFromFile(file);

                pout.print("HTTP/1.1 200 OK\r\n" +
                        "Content-Type: " + guessContentType(path) + "\r\n" +
                        "Content-Length: " + page.length + "\r\n" +
                        "Date: " + new Date() + "\r\n" +
                        "Server: FileServer 1.0\r\n\r\n");
                sendFile(files, out);
                log(socket, "200 OK");
            } catch (FileNotFoundException e) {
                errorReport(pout, socket, "404", "Not Found",
                        "The requested URL was not found on this server.");
            }
            out.flush();

        } catch (IOException | SQLException e) {
            System.err.println(e);
        }
        try {
            if (socket != null) socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void getInsertHandler(String reqUrl) throws SQLException {
        String path;
        File f;
        // Isolerar fname och lname från html-formuläret
        String reqUrl1 = reqUrl.split("\\?")[1];

        // Separerar fname och lname
        String reqUrlFirstName = reqUrl1.split("&")[0];
        String reqUrlLastName = reqUrl1.split("&")[1];

        // tar ut bara namnen som användaren har skrivit
        String reqFinalFirstName = reqUrlFirstName.split("=")[1];
        String reqFinalLastName = reqUrlLastName.split("=")[1];
        path = "core/web/action_page.html";
        f = new File(path);

        JavaSQL sql;
        sql = new JavaSQL();
        sql.Insert(reqFinalFirstName, reqFinalLastName);
    }

    @NotNull
    private static String handlePostRequest(BufferedInputStream bufferedInputStream) throws IOException, SQLException {
        String request;
        String tempReq = null;
        String tempLength = "";
        boolean hasRun = false;
        do {
            request = readLine(bufferedInputStream);
            System.out.println(request);

            if (request.startsWith("Content-Length: ")) {
                tempLength = request.split(" ")[1];
            }
            if (!hasRun) {
                String reqType2 = request.split(" ")[0];
                String reqUrl2 = request.split(" ")[1];
                System.out.println("OneTimeLoop: " + reqType2 + "  ||  " + reqUrl2);
                hasRun = true;
                tempReq = reqUrl2;
                System.out.println("tempReq: " + tempReq);
            }
        } while (!request.isEmpty());

        byte[] body = bufferedInputStream.readNBytes(Integer.parseInt(tempLength));
        request = new String(body);
        System.out.println(request);

        String reqFirstPhase1 = request.split("&")[0];
        String reqFirstPhase2 = reqFirstPhase1.split("=")[1];
        String reqLastPhase1 = request.split("&")[1];
        String reqLastPhase2 = reqLastPhase1.split("�")[0];
        String reqLastPhase3 = reqLastPhase2.split("=")[1];
        System.out.println("Firstname: " + reqFirstPhase2 + "\nLastname: " + reqLastPhase3);

        JavaSQL sql;
        sql = new JavaSQL();
        sql.Insert(reqFirstPhase2, reqLastPhase3);
        return request;
    }

    private static String readLine(BufferedInputStream inputStream) throws IOException {
        final int MAX_READ = 4096;
        byte[] buffer = new byte[MAX_READ];
        int bytesRead = 0;
        while (bytesRead < MAX_READ) {
            buffer[bytesRead++] = (byte) inputStream.read();
            if (buffer[bytesRead - 1] == '\r') {
                buffer[bytesRead++] = (byte) inputStream.read();
                if (buffer[bytesRead - 1] == '\n')
                    break;
            }
        }
        return new String(buffer, 0, bytesRead - 2, StandardCharsets.UTF_8);
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

    private static void errorReport(PrintStream pout, Socket connection, String code, String title, String msg) {
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
        else if (path.endsWith(".js"))
            return "application/javascript";
        else if (path.endsWith(".css"))
            return "text/css";
        else if (path.endsWith(".json"))
            return "application/json";
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




