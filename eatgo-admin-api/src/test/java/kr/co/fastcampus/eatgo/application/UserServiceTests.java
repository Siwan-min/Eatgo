package kr.co.fastcampus.eatgo.application;

import kr.co.fastcampus.eatgo.domain.User;
import kr.co.fastcampus.eatgo.domain.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.core.Is.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

public class UserServiceTests {

    private UserService userService;
    @Mock
    private UserRepository userRepository;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        userService = new UserService(userRepository);
    }

    @Test
    public void getUsers() {
        List<User> mockUsers = new ArrayList<>();
        mockUsers.add(User.builder()
                .email("tester@example.com")
                .name("테스터")
                .level(1L)
                .build());

        given(userRepository.findAll()).willReturn(mockUsers);

        List<User> users = userService.getUsers();

        User user = users.get(0);

        assertThat(user.getName()).isEqualTo("테스터");
    }

    @Test
    public void addUser() {
        String email = "admin@exmaple.com";
        String name = "Administrator";

        User mockUser = User.builder().email(email).name(name).build();

        given(userRepository.save(any())).willReturn(mockUser);

        User user = userService.addUser(email, name);

        assertThat(user.getName()).isEqualTo(name);
    }

    @Test
    public void updateUser() {
        Long id = 1004L;
        String email = "admin@exmaple.com";
        String name = "Superman";
        Long level = 100L;

        User mockUser = User.builder().id(id)
                .email(email)
                .name("Administrator")
                .level(1L)
                .build();

        given(userRepository.findById(id)).willReturn(Optional.of(mockUser));

        User user = userService.updateUser(id, email, name, level);

        verify(userRepository).findById(eq(id));

        assertThat(user.getName()).isEqualTo("Superman");
        assertThat(user.isAdmin()).isTrue();
    }

    @Test
    public void deactiveUser() {
        Long id = 1004L;

        User mockUser = User.builder()
                .id(id)
                .email("admin@exmaple.com")
                .name("Administrator")
                .level(100L)
                .build();

        given(userRepository.findById(id)).willReturn(Optional.of(mockUser));

        User user = userService.deactiveUser(1004L);

        verify(userRepository).findById(1004L);

        assertThat(user.isAdmin()).isFalse();
        assertThat(user.isActive()).isFalse();
    }
}