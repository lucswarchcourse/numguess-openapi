package org.openapitools.api;

import org.openapitools.model.ApiRoot;
import org.openapitools.model.Error;
import org.openapitools.model.GameCreationResponse;
import org.openapitools.model.GamesCollection;
import org.springframework.lang.Nullable;
import java.net.URI;


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
@RequestMapping("${openapi.numberGuessingGame.base-path:/numguess-restlet}")
public class GamesApiController implements GamesApi {

    private final GamesApiDelegate delegate;

    public GamesApiController(@Autowired(required = false) GamesApiDelegate delegate) {
        this.delegate = Optional.ofNullable(delegate).orElse(new GamesApiDelegate() {});
    }

    @Override
    public GamesApiDelegate getDelegate() {
        return delegate;
    }

}
