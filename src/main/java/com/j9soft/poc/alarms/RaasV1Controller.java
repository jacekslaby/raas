package com.j9soft.poc.alarms;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

// See https://spring.io/guides/gs/spring-boot/

@RestController
public class RaasV1Controller implements RaasV1 {

    private static final Logger logger = LoggerFactory.getLogger(RaasV1Controller.class);

    // @Autowired  - is not used because:
    // https://spring.io/blog/2016/04/15/testing-improvements-in-spring-boot-1-4
    // "Don’t use field injection as it just makes your tests harder to write."
    //
    private RaasDao raasDao;


    @Autowired
    RaasV1Controller(RaasDao raasDao) {

        this.raasDao = raasDao;
    }

    @Override
    @GetMapping("/v1/rawalarms")
    public String rawAlarms(@RequestParam(value = "notificationIdentifier") String notificationIdentifier,
                            @RequestAttribute(name = "partitionDefinition") RawAlarmsPartitionDefinition partitionDefinition) {

        logger.info("rawAlarms(notificationIdentifier='{}', domain='{}', adapterName='{}')",
                notificationIdentifier, partitionDefinition.getDomain(), partitionDefinition.getAdapterName());

        return this.raasDao.queryAlarm(partitionDefinition.getDomain(), partitionDefinition.getAdapterName(),
                notificationIdentifier);
    }

    // Note: Using PATCH in this way is discouraged in  https://williamdurand.fr/2014/02/14/please-do-not-patch-like-an-idiot/
    //   and yet neither PUT nor POST fit here.  (because client can use this method as 'upsert', i.e. create or partially update)
    //
    @Override
    @PatchMapping("/v1/rawalarms/{notificationIdentifier}")
    public void patchRawAlarm(@PathVariable("notificationIdentifier") String notificationIdentifier,
                              @RequestAttribute(name = "partitionDefinition") RawAlarmsPartitionDefinition partitionDefinition,
                              @RequestBody String valueObjectAsJson) {

        logger.info("patchRawAlarm(notificationIdentifier='{}', domain='{}', adapterName='{}')",
                notificationIdentifier, partitionDefinition.getDomain(), partitionDefinition.getAdapterName());

        this.raasDao.createOrPatchAlarm(partitionDefinition.getDomain(), partitionDefinition.getAdapterName(),
                notificationIdentifier, valueObjectAsJson);
    }

    @PostConstruct
    public void init() {
        Assert.notNull(raasDao, "raasDao is null!");
        logger.info("raasDao is not null - OK");
    }

}