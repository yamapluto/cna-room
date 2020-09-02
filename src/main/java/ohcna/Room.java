package ohcna;

import javax.persistence.*;
import org.springframework.beans.BeanUtils;
import java.util.List;

@Entity
@Table(name="Room_table")
public class Room {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Long id;
    private String name;
    private String floor;

    @PostPersist
    public void onPostPersist(){
        RoomCreated roomCreated = new RoomCreated();
        BeanUtils.copyProperties(this, roomCreated);
        roomCreated.publishAfterCommit();

        //Following code causes dependency to external APIs
        // it is NOT A GOOD PRACTICE. instead, Event-Policy mapping is recommended.

        ohcna.external.Point point = new ohcna.external.Point();
        // mappings goes here
        point.setId(roomCreated.getId());
        point.setStatus("created");
        point.setPoint(0);
        System.out.println("##### point onPostPersist : " + point.getId()+"<<<<<<<");
        RoomApplication.applicationContext.getBean(ohcna.external.PointService.class)
            .pointCreate(point);


    }

    @PostUpdate
    public void onPostUpdate(){
        RoomChangeed roomChangeed = new RoomChangeed();
        BeanUtils.copyProperties(this, roomChangeed);
        roomChangeed.publishAfterCommit();


    }

    @PostRemove
    public void onPostRemove(){
        System.out.println("##### point onPostRemove <<<<<<<");
        RoomDeleted roomDeleted = new RoomDeleted();
        BeanUtils.copyProperties(this, roomDeleted);
        roomDeleted.publishAfterCommit();


    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    public String getFloor() {
        return floor;
    }

    public void setFloor(String floor) {
        this.floor = floor;
    }
}
