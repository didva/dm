package com.epam.trainings.spring.core.dm.dao.impl.inmemory;

import com.epam.trainings.spring.core.dm.dao.AuditoriumDao;
import com.epam.trainings.spring.core.dm.model.Auditorium;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.IOException;
import java.util.*;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toList;


public class AuditoriumDaoInMemoryImpl implements AuditoriumDao {

    private Set<Auditorium> auditoriumList = new HashSet<>();

    public AuditoriumDaoInMemoryImpl(String propertiesLocation) throws IOException {
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver(getClass().getClassLoader());
        Resource[] resources = resolver.getResources("classpath*:" + propertiesLocation + "/*.properties");
        for (Resource resource : resources) {
            Properties properties = new Properties();
            properties.load(resource.getInputStream());

            Auditorium auditorium = getAuditorium(properties);
            auditoriumList.add(auditorium);
        }
    }

    @Override
    public List<Auditorium> findAll() {
        return new ArrayList<>(auditoriumList);
    }

    @Override
    public Auditorium findByName(String name) {
        return auditoriumList.stream().filter(a -> a.getName().equals(name)).findFirst().orElse(null);
    }

    private Auditorium getAuditorium(Properties properties) {
        Auditorium auditorium = new Auditorium();
        auditorium.setName(properties.getProperty("name"));
        auditorium.setSeatsNumber(Integer.parseInt(properties.getProperty("seats")));

        String[] vipSeatsArray = properties.getProperty("vipSeats").split(",");
        List<Integer> vipSeats = asList(vipSeatsArray).stream().map(Integer::parseInt).collect(toList());
        auditorium.setVipSeats(new HashSet<>(vipSeats));
        return auditorium;
    }

}
