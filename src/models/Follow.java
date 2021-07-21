
package models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;


@Table(name = "follow")

@NamedQueries({
    @NamedQuery(
        name = "getFollower",
        query = "SELECT f FROM Follow AS f WHERE f.follow_id = :employee ORDER BY f.id DESC"
    ),
    @NamedQuery(
            name = "getFollowBoolean",
            query = "SELECT f FROM Follow AS f WHERE f.follow_id = :employee AND f.follower_id=:follower_id ORDER BY f.id DESC"
        )
})
@Entity
public class Follow {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "follow_id", nullable = false)
    private Employee follow_id;

    @Column(name = "follower_id", nullable = false)
    private Integer follower_id;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Employee getFollow_id() {
        return follow_id;
    }

    public void setFollow_id(Employee follow_id) {
        this.follow_id = follow_id;
    }

    public Integer getFollower_id() {
        return follower_id;
    }

    public void setFollower_id(Integer follower_id) {
        this.follower_id = follower_id;
    }
}
