package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.dto.UserRoleUpdateRequest;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.service.AuthService;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.session.SessionInformation;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BasicAuthService implements AuthService {

  private final UserRepository userRepository;
  private final UserMapper userMapper;
  private final SessionRegistry sessionRegistry;

  @Override
  @Transactional
  @PreAuthorize("hasRole('ADMIN')")
  public UserDto.Response updateRole(UserRoleUpdateRequest request) {
    log.debug("권한 수정 요청: userId={}, newRole={}", request.userId(), request.newRole());
    expireUserSessions(request.userId());
    return applyRole(request);
  }

  @Override
  @Transactional
  public UserDto.Response updateRoleInternal(UserRoleUpdateRequest request) {
    log.debug("내부 권한 수정: userId={}, newRole={}", request.userId(), request.newRole());
    return applyRole(request);
  }

  private UserDto.Response applyRole(UserRoleUpdateRequest request) {
    User user = userRepository.findById(request.userId())
        .orElseThrow(() -> UserNotFoundException.withId(request.userId()));
    user.updateRole(request.newRole());
    log.info("사용자 권한 수정 완료: userId={}, newRole={}", request.userId(), request.newRole());
    return userMapper.toDto(user);
  }

  private void expireUserSessions(UUID userId) {
    sessionRegistry.getAllPrincipals().stream()
        .filter(principal -> principal instanceof DiscodeitUserDetails)
        .map(DiscodeitUserDetails.class::cast)
        .filter(details -> details.getUserDto().id().equals(userId))
        .flatMap(details -> sessionRegistry.getAllSessions(details, false).stream())
        .forEach(SessionInformation::expireNow);
  }
}