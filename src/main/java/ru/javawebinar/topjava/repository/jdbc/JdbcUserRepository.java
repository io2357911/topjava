package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.util.*;

import static ru.javawebinar.topjava.util.ValidationUtil.validate;

@Repository
@Transactional(readOnly = true)
public class JdbcUserRepository implements UserRepository {

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;

    @Autowired
    public JdbcUserRepository(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional
    public User save(User user) {
        validate(user);

        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
        } else {
            if (namedParameterJdbcTemplate.update("""
                       UPDATE users SET name=:name, email=:email, password=:password, 
                       registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id
                    """, parameterSource) == 0) {
                return null;
            }
            jdbcTemplate.update("DELETE FROM user_roles WHERE user_id=?", user.getId());
        }
        addRoles(user);
        return user;
    }

    private void addRoles(User user) {
        var batchValues = user.getRoles().stream()
                .map(role -> {
                            var map = new HashMap<>();
                            map.put("user_id", user.getId());
                            map.put("role", role.name());
                            return map;
                        }
                ).toArray(Map[]::new);
        namedParameterJdbcTemplate.batchUpdate("insert into user_roles (user_id, role) values(:user_id,:role)",
                batchValues);
    }

    @Override
    @Transactional
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM users LEFT JOIN user_roles ur on users.id = ur.user_id WHERE id=?", extractor, id);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public User getByEmail(String email) {
        List<User> users = jdbcTemplate.query(
                "SELECT * FROM users LEFT JOIN user_roles ur on users.id = ur.user_id WHERE email=?", extractor, email);
        return DataAccessUtils.singleResult(users);
    }

    @Override
    public List<User> getAll() {
        return jdbcTemplate.query(
                "SELECT * FROM users AS u LEFT JOIN user_roles AS r ON u.id = r.user_id ORDER BY name, email", extractor);
    }

    private final ResultSetExtractor<List<User>> extractor = rs -> {
        Map<Integer, User> map = new LinkedHashMap<>();

        while (rs.next()) {
            var id = rs.getInt("id");

            var user = map.get(id);
            if (user == null) {
                user = new User(
                        id,
                        rs.getString("name"),
                        rs.getString("email"),
                        rs.getString("password"),
                        rs.getInt("calories_per_day"),
                        rs.getBoolean("enabled"),
                        rs.getDate("registered"),
                        Collections.EMPTY_SET);
                map.put(id, user);
            }

            var role = rs.getString("role");
            if (role != null) {
                var roles = user.getRoles();
                roles.add(Role.valueOf(role));
                user.setRoles(roles);
            }
        }
        return List.copyOf(map.values());
    };
}
