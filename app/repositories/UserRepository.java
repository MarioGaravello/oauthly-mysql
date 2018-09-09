package repositories;

import config.Utils;
import models.User;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import io.ebean.*;

@Singleton
public class UserRepository {

    private static final Finder<Long, User> find = new Finder<>(User.class);

    public User findById(Long id) {
      return find.query().where().eq("id", id).findOne();
    }

    public void save(User u){
        Ebean.save(u);
    }

    public User findByUsernameNormalized(String usernameNormalized) {
      return find.query().where().eq("usernameNormalized",usernameNormalized).findOne();
    }

    public User findByEmail(String email) {
      return find.query().where().eq("email",email).findOne();
    }

    public User findByUsernameOrEmail(String login) {
        String normalizedUsername = Utils.normalizeUsername(login);
        User user = findByUsernameNormalized(normalizedUsername);
        if (user == null) {
            user = findByEmail(Utils.normalizeEmail(login));
        }
        return user;
    }

    public List<User> findByUsernameOrEmailMulti(String login) {
      String normalizedUsername = Utils.normalizeUsername(login);
      String email = Utils.normalizeEmail(login);
      List<User> list = new ArrayList<>();
      Iterator<User> iterator = find.query().where().eq("usernameNormalized", normalizedUsername).findIterate();
      while (iterator.hasNext()) {
          list.add(iterator.next());
      }
      iterator = find.query().where().eq("email", email).findIterate();
      while (iterator.hasNext()) {
          list.add(iterator.next());
      }
      return list;
    }

    public Iterable<User> findAll() {
      return find.query().where().findList();
    }

    public Iterable<User> findByUserId(Long userId) {
      return find.query().where().eq("userId", userId).findList();
    }

    public long count() {
      return find.query().where().findCount();
    }
}
