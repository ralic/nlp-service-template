package org.librairy.service.nlp.controllers;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.apache.avro.AvroRemoteException;
import org.librairy.service.nlp.facade.model.Annotation;
import org.librairy.service.nlp.facade.model.NlpService;
import org.librairy.service.nlp.facade.rest.model.AnnotationRequest;
import org.librairy.service.nlp.facade.rest.model.AnnotationResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/annotate")
@Api(tags="/annotate", description="list of annotations from a text")
public class RestAnnotateController {

    private static final Logger LOG = LoggerFactory.getLogger(RestAnnotateController.class);

    @Autowired
    NlpService service;

    @PostConstruct
    public void setup(){

    }

    @PreDestroy
    public void destroy(){

    }

    @ApiOperation(value = "filter words by PoS and return their annotations", nickname = "postAnnotate", response=AnnotationResult.class)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = AnnotationResult.class),
    })
    @RequestMapping(method = RequestMethod.POST, produces = "application/json")
    public AnnotationResult analyze(@RequestBody AnnotationRequest annotationRequest)  {
        try {
            List<Annotation> annotations = service.annotate(annotationRequest.getText(), annotationRequest.getFilter());
            return new AnnotationResult(annotations.stream().map(a -> new org.librairy.service.nlp.facade.rest.model.Annotation(a)).collect(Collectors.toList()));
        } catch (AvroRemoteException e) {
            throw new RuntimeException(e);
        }
    }

}
