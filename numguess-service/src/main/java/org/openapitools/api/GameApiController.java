package org.openapitools.api;

import org.openapitools.model.Error;
import org.openapitools.model.GameState;
import org.openapitools.model.GuessResult;
import org.springframework.lang.Nullable;
import java.util.UUID;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import jakarta.validation.constraints.*;
import jakarta.validation.Valid;

import java.util.List;
import java.util.Map;
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
