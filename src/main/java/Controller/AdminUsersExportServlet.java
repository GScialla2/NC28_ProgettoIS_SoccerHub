package Controller;

import Model.UserDAO;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

@WebServlet("/AdminUsersExportServlet")
public class AdminUsersExportServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String providedToken = req.getParameter("token");
        String expectedToken = getServletContext().getInitParameter("ADMIN_EXPORT_TOKEN");
        if (expectedToken == null) {
            expectedToken = "CHANGE_ME_SECURE_TOKEN"; // fallback if not configured
        }
        if (providedToken == null || !expectedToken.equals(providedToken)) {
            resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
            resp.setContentType("text/plain;charset=UTF-8");
            resp.getWriter().write("Forbidden: invalid or missing token");
            return;
        }

        String format = req.getParameter("format");
        if (format == null) format = "csv";

        ArrayList<String[]> creds = UserDAO.doRetrieveAllCredentials();

        if ("json".equalsIgnoreCase(format)) {
            resp.setContentType("application/json;charset=UTF-8");
            PrintWriter out = resp.getWriter();
            out.print("[");
            for (int i = 0; i < creds.size(); i++) {
                String[] c = creds.get(i);
                out.print("{\"email\":\"" + escapeJson(c[0]) + "\",\"password_hash\":\"" + escapeJson(c[1]) + "\"}");
                if (i < creds.size() - 1) out.print(",");
            }
            out.print("]");
            return;
        }

        // default CSV
        resp.setContentType("text/csv;charset=UTF-8");
        PrintWriter out = resp.getWriter();
        out.println("email,password_hash");
        for (String[] c : creds) {
            out.println(escapeCsv(c[0]) + "," + escapeCsv(c[1]));
        }
    }

    private String escapeCsv(String s) {
        if (s == null) return "";
        boolean needsQuotes = s.contains(",") || s.contains("\n") || s.contains("\r") || s.contains("\"");
        String escaped = s.replace("\"", "\"\"");
        return needsQuotes ? "\"" + escaped + "\"" : escaped;
    }

    private String escapeJson(String s) {
        if (s == null) return "";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            switch (c) {
                case '"': sb.append("\\\""); break;
                case '\\': sb.append("\\\\"); break;
                case '\b': sb.append("\\b"); break;
                case '\f': sb.append("\\f"); break;
                case '\n': sb.append("\\n"); break;
                case '\r': sb.append("\\r"); break;
                case '\t': sb.append("\\t"); break;
                default:
                    if (c < 0x20) {
                        sb.append(String.format("\\u%04x", (int)c));
                    } else {
                        sb.append(c);
                    }
            }
        }
        return sb.toString();
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doGet(req, resp);
    }
}
