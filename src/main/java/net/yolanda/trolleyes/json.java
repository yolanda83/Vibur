/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net.yolanda.trolleyes;

import com.google.gson.Gson;
import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.vibur.dbcp.ViburDBCPDataSource;

/**
 *
 * @author Yolanda
 */
public class json extends HttpServlet {

    /**
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
     * methods.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset-UTF-8");

//        try (PrintWriter out = response.getWriter()) {
//            HttpSession oSession = request.getSession();
            Gson gson = new Gson();
            String strJson = "";
            String strOp = request.getParameter("op");

            if (strOp != null) {
                if (!strOp.equalsIgnoreCase("")) {

                    if (strOp.equalsIgnoreCase("connect")) {
                        // conexion a la base de datos
//                        Connection connection = null;

                        try {
                            Class.forName("com.mysql.jdbc.Driver");

                        } catch (Exception ex) {
                            strJson = "{\"status\":500,\"msg\":\"jdbc driver not found\"}";
                        }

                        ViburDBCPDataSource config = new ViburDBCPDataSource();
//                        ViburDBCPDataSource ds = new ViburDBCPDataSource();
                        config.setJdbcUrl("jdbc:mysql://localhost:3306/trolleyes");
//                        config.setJdbcUrl("jdbc:mysql://localhost/trolleyes");
                        config.setUsername("root2");
                        config.setPassword("bitnami");

                        config.setPoolInitialSize(10);
                        config.setPoolMaxSize(100);

                        config.setConnectionIdleLimitInSeconds(30);
                        config.setTestConnectionQuery("isValid");

                        config.setLogQueryExecutionLongerThanMs(500);
                        config.setLogStackTraceForLongQueryExecution(true);

                        config.setStatementCacheMaxSize(200);
                        config.start();

                        try {
//                            ViburDBCPDataSource oConnectionPool = ds;
//                            Connection oConnection = (Connection) ds.getConnection();
//                            connection = ds.getConnection();
                            Connection oConnection = (Connection) config.getConnection();
//                            oConnection = (Connection) oConnectionPool.getConnection();

                            strJson = "{\"status\":200,\"msg\":\"vibur Connection OK loli\"}";
                            oConnection.close();
                        } catch (Exception ex) {
                            strJson = "{\"status\":500,\"msg\":\"Bad vibur Connection\"}";
                          
                        }

                    }

                    HttpSession oSession = request.getSession();

                    if (strOp.equalsIgnoreCase("login")) {
                        String strUser = request.getParameter("user");
                        String strPass = request.getParameter("pass");
                        if (strUser.equalsIgnoreCase("yolanda") && strPass.equals("1234")) {
                            oSession.setAttribute("sessionVar", strUser);
                            strJson = "{\"status\":200,\"msg\":\"" + strUser + "\"}";
                        } else {
                            strJson = "{\"status\":401,\"msg\":\"Authentication error\"}";
                        }
                    }
                    if (strOp.equalsIgnoreCase("logout")) {
                        oSession.invalidate();
                        strJson = "{\"status\":200,\"msg\":\"Session is closed\"}";
                    }
                    if (strOp.equalsIgnoreCase("check")) {
                        String strUserName = (String) oSession.getAttribute("sessionVar");
                        if (strUserName != null) {
                            strJson = "{\"status\":200,\"msg\":\"" + strUserName + "\"}";
                        } else {
                            strJson = "{\"status\":401,\"msg\":\"Authentication error\"}";
                        }
                    }
                    if (strOp.equalsIgnoreCase("getSecret")) {
                        String strUserName = (String) oSession.getAttribute("sessionVar");
                        if (strUserName != null) {
                            strJson = "{\"status\":200,\"msg\":\"Rafa es el mejor.\"}";
                        } else {
                            strJson = "{\"status\":401,\"msg\":\"Authentication error\"}";

                        }
                    }
                } else {
                    strJson = "{\"status\":200,\"msg\":\" + \"Ok.\"}";

                }
            } else {
                strJson = "{\"status\":200,\"msg\":\" + \"Ok.\"}";

            }
            response.getWriter().append(strJson).close();
            //response.getWriter().append(gson.toJson(strJson)).close();

//        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
