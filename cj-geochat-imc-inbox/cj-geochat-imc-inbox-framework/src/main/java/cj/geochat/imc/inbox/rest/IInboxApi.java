package cj.geochat.imc.inbox.rest;

import jakarta.servlet.http.HttpServletRequest;

public interface IInboxApi {
    void inbox(HttpServletRequest request, String line, String host, String body);

    void inboxRaw(String frameRaw);
}
