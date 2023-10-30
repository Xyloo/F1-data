package pl.pollub.f1data.Models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 * Generic response object with a message
 */
@AllArgsConstructor
@Getter
@Setter
public class MessageResponse {
    private String Message;
}
