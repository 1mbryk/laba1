package servlets;

import entity.ChatUser;

import java.io.PrintWriter;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LoginServlet extends ChatServlet
{
    private static final long SERIAL_VERSION_UID = 1;
    private int session_timeout = 10 * 60;

    public void init() throws ServletException
    {
        super.init();

        String value = getServletConfig().getInitParameter("SESSION_TIMEOUT");
        if (value != null)
        {
            session_timeout = Integer.parseInt(value);
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException
    {
        String name = (String) request.getSession().getAttribute("name"); // check is users name in session
        String error_message = (String) request.getSession().getAttribute("error");
        String prev_session_id = null;

        if (name == null) // try to get name from cookie
        {
            for (Cookie cookie : request.getCookies())
            {
                if (cookie.getName().equals("session_id"))
                {
                    prev_session_id = cookie.getValue();
                    break;
                }
            }
            if (prev_session_id != null)
            {
                for (ChatUser user : active_users.values())
                {
                    if (user.getSessionId().equals(prev_session_id))
                    {
                        name = user.getName();
                        user.setSessionId(request.getSession().getId());
                    }
                }
            }
        }

        if (name != null && !"".equals(name))
        {
            error_message = processLogonAttempt(name, request, response);
        }

        response.setCharacterEncoding("utf8");

        PrintWriter pw = response.getWriter();
        pw.println("<form action='/laba8/' method='post'>Enter name:" + "<input type='text' name='name' value=''><input type='submit' value='Go to " + "chat'>");
        pw.println("</form></body></html>");
        request.getSession().setAttribute("error", null);

    }

    protected void doPost(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        String name = (String) request.getParameter("name");
        String errorMessage = null;
        if (name == null || "".equals(name))
        {
            errorMessage = "User name can't be empty";
        } else
        {
            errorMessage = processLogonAttempt(name, request, response);

        }
        if (errorMessage != null)
        {
            request.getSession().setAttribute("name", null);
            request.getSession().setAttribute("error", errorMessage);
            response.sendRedirect(response.encodeRedirectURL("/laba8/"));
        }
    }

    String processLogonAttempt(String name, HttpServletRequest request,
                               HttpServletResponse response) throws IOException
    {
        String sessionId = request.getSession().getId();
        ChatUser aUser = active_users.get(name);


        if (aUser.getSessionId().equals(sessionId) ||
                aUser.getLastInteractionTime() < (Calendar.getInstance().getTimeInMillis() -
                        session_timeout * 1000))
        {

            request.getSession().setAttribute("name", name);

            aUser.setLastInteractionTime(Calendar.getInstance().getTimeInMillis());
            Cookie session_id_cookie = new Cookie("session_id", sessionId);
            session_id_cookie.setMaxAge(60 * 60 * 24 * 365);
            response.addCookie(session_id_cookie);
            response.sendRedirect(response.encodeRedirectURL("/laba8/view.html"));
            return null;
        } else
        {
            return "Sorry, name <strong>" + name + "</strong> was " +
                    "taken!Please select another name!";
        }
    }
}
