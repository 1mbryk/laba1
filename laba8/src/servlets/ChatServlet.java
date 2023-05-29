package servlets;

import java.util.ArrayList;
import java.util.HashMap;

import entity.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

public class ChatServlet extends HttpServlet
{
    private static final long SERIAL_VERSION_UID = 1;
    protected HashMap<String, ChatUser> active_users;
    protected ArrayList<ChatMessage> messages;

    @SuppressWarnings("unchecked")
    public void init() throws ServletException
    {
        super.init();
        active_users = (HashMap<String, ChatUser>) getServletContext().getAttribute("active_users");
        messages = (ArrayList<ChatMessage>) getServletContext().getAttribute("messages");

        if (active_users == null)
        {
            active_users = new HashMap<>();
            getServletContext().setAttribute("active_users", active_users);
        }

        if (messages == null)
        {
            messages = new ArrayList<>(100);
            getServletContext().setAttribute("messages", messages);
        }
    }
}

