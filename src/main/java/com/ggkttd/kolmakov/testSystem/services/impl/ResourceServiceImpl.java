package com.ggkttd.kolmakov.testSystem.services.impl;

import com.ggkttd.kolmakov.testSystem.domain.Resource;
import com.ggkttd.kolmakov.testSystem.exceptions.NotFoundException;
import com.ggkttd.kolmakov.testSystem.repo.ResourceRepo;
import com.ggkttd.kolmakov.testSystem.services.ResourceService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Files;

@Service
public class ResourceServiceImpl implements ResourceService {
    private static final Logger LOGGER = Logger.getLogger(ResourceServiceImpl.class);

    @Value("${chunk.length:1048576}")
    private int chunkLength;

    @Autowired
    private ResourceRepo resourceRepo;

    @Override
    public byte[] getVideoStream(Resource resource, int startPosition) {
        File file = new File(resource.getPath() + resource.getFileName());
        byte[] chunk = null;

        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            if (startPosition >= 0) {
                randomAccessFile.seek(startPosition);

                if (startPosition + chunkLength < resource.getFileLength()) {

                    chunk = new byte[chunkLength];
                    randomAccessFile.read(chunk, 0, chunkLength);

                } else {
                    chunk = new byte[Math.toIntExact(randomAccessFile.length() - startPosition)];
                    randomAccessFile.read(chunk, 0, Math.toIntExact(randomAccessFile.length() - startPosition));

                }

            } else {
                chunk = new byte[-startPosition];

                //seek pointer to file.length - startPosition
                randomAccessFile.seek(randomAccessFile.length() + startPosition - 1);
                randomAccessFile.read(chunk, 0, -startPosition);
            }
        } catch (IOException e) {
            LOGGER.warn(e);
        }

        return chunk;
    }

    @Override
    public byte[] getVideoStream(Resource resource, int startPosition, int endPosition) {
        File file = new File(resource.getPath() + resource.getFileName());
        byte[] chunk = null;
        int delta = endPosition - startPosition;
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            if (delta > 0) {
                chunk = new byte[delta];

                randomAccessFile.seek(startPosition);
                randomAccessFile.read(chunk, 0, delta);

            } else if (delta == 0) {
                chunk = new byte[1];

                randomAccessFile.seek(startPosition);
                randomAccessFile.read(chunk, 0, 1);

            } else if (delta < 0) {
                throw new RuntimeException("Invalid range. First value " + startPosition + " must be greater or equals to " + endPosition);
            }
        } catch (IOException e) {
            LOGGER.warn(e);
        }

        return chunk;
    }

    @Override
    public byte[] getVideoStream(Resource resource) {
        try {
            return Files.readAllBytes(new File(resource.getPath() + resource.getFileName()).toPath());
        } catch (IOException e) {
            LOGGER.warn(e);
        }
        return new byte[1];
    }

    @Override
    public Resource getOne(Long id) {
        return resourceRepo.findById(id).orElseThrow(() -> new NotFoundException("RESOURCE #" + id + " NOT FOUND"));
    }
}
