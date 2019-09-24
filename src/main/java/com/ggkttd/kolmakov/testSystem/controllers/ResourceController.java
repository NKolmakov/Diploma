package com.ggkttd.kolmakov.testSystem.controllers;

import com.ggkttd.kolmakov.testSystem.domain.Resource;
import com.ggkttd.kolmakov.testSystem.services.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Controller
@RequestMapping(value = "/resources")
public class ResourceController {
    @Autowired
    private ResourceService resourceService;

    @Value("${chunk.length:1048576}")
    private int chunkLength;

    @GetMapping(value = "{id}")
    @ResponseBody
    public byte[] getResourceStream(@PathVariable("id") Long id, HttpServletRequest request, HttpServletResponse response) {
        String strRange = request.getHeader("Range");
        Resource resource = resourceService.getOne(id);
        byte[] chunk = null;
        int startPosition = 0;
        int status = 200;

        if (strRange != null) {
            strRange = strRange.replaceAll(".*bytes.*=\\s*", "");
            //range looks like Range: 0-1024
            if (strRange.matches("\\d+-\\d+")) {

                String[] ranges = strRange.split("-");
                startPosition = Integer.parseInt(ranges[0]);
                int endPosition = Integer.parseInt(ranges[1]);

                chunk = resourceService.getVideoStream(resource, endPosition, startPosition);

                //range looks like Range: 0-
            } else if (strRange.matches("\\d+-")) {

                startPosition = Integer.parseInt(strRange.replaceAll("-", ""));
                chunk = resourceService.getVideoStream(resource, startPosition);

                //range looks like Range: -152
            } else if (strRange.matches("-\\d+")) {
                startPosition = Integer.parseInt(strRange.replaceAll("-", ""));
                chunk = resourceService.getVideoStream(resource, -startPosition);
            }
            status = 206;
        } else {
            //give all file
            chunk = resourceService.getVideoStream(resource);
        }

        response.setStatus(status);
        response.setHeader("Content-Type", resource.getType());
        response.setContentLength(chunk.length);
        response.setHeader("Accept-Range", "bytes");
        response.setHeader("Content-Range", "bytes " + startPosition + "-" + (startPosition + chunk.length - 1) + "/" + resource.getFileLength());
        response.setHeader("Content-Transfer-Encoding", "binary");
        response.setHeader("Cache-Control", "");
        response.setHeader("Connection", "close");

        return chunk;
    }
}
