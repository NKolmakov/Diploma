package com.ggkttd.kolmakov.testSystem.services;

import com.ggkttd.kolmakov.testSystem.domain.Resource;

public interface ResourceService {
    byte[] getVideoStream(Resource resource,int startPosition);
    byte[] getVideoStream(Resource resource,int startPosition,int endPosition);
    byte[] getVideoStream(Resource resource);
    Resource getOne(Long id);
}
