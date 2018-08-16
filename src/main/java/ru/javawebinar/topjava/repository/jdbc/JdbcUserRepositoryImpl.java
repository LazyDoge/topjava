package ru.javawebinar.topjava.repository.jdbc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.*;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.javawebinar.topjava.model.Role;
import ru.javawebinar.topjava.model.User;
import ru.javawebinar.topjava.repository.UserRepository;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

@Repository
@Transactional(readOnly = true, value = "jdbcTransactionManager")
public class JdbcUserRepositoryImpl implements UserRepository {

    private static final BeanPropertyRowMapper<User> ROW_MAPPER = BeanPropertyRowMapper.newInstance(User.class);
    private static final ResultSetExtractor<User> rse = rs -> {
        User user = new User();
        user.setId(rs.getInt("id"));
        user.setCaloriesPerDay(rs.getInt("calories_per_day"));
        user.setName(rs.getString("name"));
        user.setEmail(rs.getString("email"));
        user.setPassword(rs.getString("password"));
        user.setRegistered(rs.getDate("registered"));
        user.setEnabled(rs.getBoolean("enabled"));
        HashSet<Role> roles = new HashSet<>();
        roles.add(Role.valueOf(rs.getString("role")));
        while (rs.next()) {
            roles.add(Role.valueOf(rs.getString("role")));
        }
        user.setRoles(roles);
        return user;
    };
    public static final HashSet<Role> ROLES = new HashSet<>();

    private final JdbcTemplate jdbcTemplate;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    private final SimpleJdbcInsert insertUser;


    @Autowired
    public JdbcUserRepositoryImpl(JdbcTemplate jdbcTemplate, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.insertUser = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("users")
                .usingGeneratedKeyColumns("id");

        this.jdbcTemplate = jdbcTemplate;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }

    @Override
    @Transactional("jdbcTransactionManager")
    public User save(User user) {
        BeanPropertySqlParameterSource parameterSource = new BeanPropertySqlParameterSource(user);
        ArrayList<Role> roles = new ArrayList<Role>(user.getRoles());

        if (user.isNew()) {
            Number newKey = insertUser.executeAndReturnKey(parameterSource);
            user.setId(newKey.intValue());
            jdbcTemplate.batchUpdate("INSERT INTO user_roles (user_id, role) VALUES (?, ?)", new BatchPreparedStatementSetter() {
                @Override
                public void setValues(PreparedStatement ps, int i) throws SQLException {
                    Role role = roles.get(i);
                    ps.setInt(1, user.getId());
                    ps.setString(2, String.valueOf(role));
                }

                @Override
                public int getBatchSize() {
                    return roles.size();
                }
            });

        } else if (namedParameterJdbcTemplate.update(
                "UPDATE users SET name=:name, email=:email, password=:password, " +
                        "registered=:registered, enabled=:enabled, calories_per_day=:caloriesPerDay WHERE id=:id", parameterSource) == 0) {
            return null;
        }
        return user;
    }

    @Override
    @Transactional("jdbcTransactionManager")
    public boolean delete(int id) {
        return jdbcTemplate.update("DELETE FROM users WHERE id=?", id) != 0;
    }

//    @Override
//    public User get(int id) {
//        List<User> users = jdbcTemplate.query("SELECT *" +
//                " FROM users u JOIN user_roles ur ON u.id = ur.user_id WHERE id=?", (RowMapper<User>) (rs, rowNum) -> {
//            User user = new User();
//            user.setId(rs.getInt("id"));
//            user.setCaloriesPerDay(rs.getInt("calories_per_day"));
//            user.setName(rs.getString("name"));
//            user.setEmail(rs.getString("email"));
//            user.setPassword(rs.getString("password"));
//            user.setRegistered(rs.getDate("registered"));
//            user.setEnabled(rs.getBoolean("enabled"));
//            HashSet<Role> roles = new HashSet<>();
//            roles.add(Role.valueOf(rs.getString("role")));
//            while (rs.next()) {
//                roles.add(Role.valueOf(rs.getString("role")));
//            }
//            user.setRoles(roles);
//            return user;
//        }, id);
//        return DataAccessUtils.singleResult(users);
//    }


    @Override
    public User get(int id) {
        List<User> users = jdbcTemplate.query("SELECT *" +
                " FROM users u JOIN user_roles ur ON u.id = ur.user_id WHERE id=?", (rs, rowNum) -> rse.extractData(rs), id);
        return DataAccessUtils.singleResult(users);
    }


    @Override
    public User getByEmail(String email) {
//        return jdbcTemplate.queryForObject("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        List<User> users = jdbcTemplate.query("SELECT * FROM users WHERE email=?", ROW_MAPPER, email);
        User user = DataAccessUtils.singleResult(users);
        Integer id = Objects.requireNonNull(user).getId();
        List<Role> roles = jdbcTemplate.query("SELECT role FROM  user_roles ur WHERE user_id=?",
                (rs, rowNum) -> Role.valueOf(rs.getString("role")), id);
        user.setRoles(roles);

        return user;
    }

    @Override
    public List<User> getAll() {
        HashMap<Integer, Set<Role>> map = new HashMap<>();
        List<User> users = jdbcTemplate.query("SELECT * FROM users ORDER BY name, email", ROW_MAPPER);
        jdbcTemplate.query("SELECT * FROM user_roles", (RowMapper<Role>) (rs, rowNum) -> {
            int id = rs.getInt("user_id");
            Role role = Role.valueOf(rs.getString("role"));
            map.computeIfAbsent(id, v -> new HashSet<>()).add(role);
            return null;
        });

        users.forEach(user -> user.setRoles(map.getOrDefault(user.getId(), Collections.emptySet())));

        return users;
    }
}
