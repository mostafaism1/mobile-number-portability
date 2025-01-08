package com.github.mostafaism1.mobile_number_portability.app.request;

public record CreatePortRequestCommand(String number, String donor, String recipient, String requestedBy) {

}
