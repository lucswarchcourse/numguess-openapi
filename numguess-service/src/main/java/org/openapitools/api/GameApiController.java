package org.openapitools.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;
import jakarta.annotation.Generated;

@Generated(value = "org.openapitools.codegen.languages.SpringCodegen", date = "2025-11-10T20:40:10.675029-06:00[America/Chicago]", comments = "Generator version: 7.17.0")
@Controller
@RequestMapping("${openapi.numberGuessingGame.base-path:/numguess}")
public class GameApiController implements GameApi {

    private final GameApiDelegate delegate;

    public GameApiController(@Autowired(required = false) GameApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new GameApiDelegate() {});
    }

    @Override
    public GameApiDelegate getDelegate() {
        return delegate;
    }

}
