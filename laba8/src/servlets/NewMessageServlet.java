package servlets;

import java.io.IOException;
import java.util.Calendar;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import entity.ChatMessage;
import entity.ChatUser;

public class NewMessageServlet extends ChatServlet
{

    private static final long serialVersionUID = 1L;

    protected void doPost(HttpServletRequest request, HttpServletResponse
            response) throws ServletException, IOException
    {
        request.setCharacterEncoding("UTF-8");
        String message = (String) request.getParameter("message");
        if (message != null && !"".equals(message))
        {
            ChatUser author = active_users.get((String) request.getSession().getAttribute("name"));
            synchronized (messages)
            {
                messages.add(new ChatMessage(message, author, Calendar.getInstance().getTimeInMillis()));
            }
        }
        response.sendRedirect("laba8/message.html");
    }
}