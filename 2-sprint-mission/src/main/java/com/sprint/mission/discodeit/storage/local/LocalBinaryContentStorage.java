package com.sprint.mission.discodeit.storage.local;

import com.sprint.mission.discodeit.dto.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class LocalBinaryContentStorage implements BinaryContentStorage {

  @Override
  public UUID put(UUID id, byte[] bytes) {
    return id;
  }

  @Override
  public InputStream get(UUID id) {
    return null;
  }

  @Override
  public ResponseEntity<?> download(BinaryContentDto.Response dto) {
    return null;
  }
}