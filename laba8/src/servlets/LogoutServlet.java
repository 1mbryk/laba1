package servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.ChatUser;

public class LogoutServlet extends ChatServlet
{

    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException
    {
        String name = (String) request.getSession().getAttribute("name");
        if (name != null)
        {
            ChatUser aUser = active_users.get(name);
            if (aUser.getSessionId().equals((String) request.getSession().getId()))
            {
                synchronized (active_users)
                {
                    active_users.remove(name);
                }
                request.getSession().setAttribute("name", null);
                response.addCookie(new Cookie("session_id", null));
                response.sendRedirect(response.encodeRedirectURL("/laba8/"));
            } else
            {
                response.sendRedirect(response.encodeRedirectURL("/laba8/view.html"));
            }
        } else
        {
            response.sendRedirect(response.encodeRedirectURL("/laba8/view.html"));
        }
    }
}

