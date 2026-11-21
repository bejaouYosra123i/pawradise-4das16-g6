const { Eureka } = require('eureka-js-client');

function registerWithEureka(port) {
  const eurekaUrl = new URL(process.env.EUREKA_URL || 'http://localhost:8761/eureka');
  const host = process.env.HOST_IP || 'localhost';
  const appName = 'donation-service';

  const client = new Eureka({
    instance: {
      app: appName,
      instanceId: `${appName}:${port}`,
      hostName: host,
      ipAddr: host,
      port: { '$': Number(port), '@enabled': true },
      vipAddress: appName,
      statusPageUrl: `http://${host}:${port}/health`,
      healthCheckUrl: `http://${host}:${port}/health`,
      homePageUrl: `http://${host}:${port}/`,
      dataCenterInfo: {
        '@class': 'com.netflix.appinfo.InstanceInfo$DefaultDataCenterInfo',
        name: 'MyOwn',
      },
    },
    eureka: {
      host: eurekaUrl.hostname,
      port: Number(eurekaUrl.port) || 8761,
      servicePath: eurekaUrl.pathname.replace(/\/$/, '') + '/apps/',
    },
  });

  client.start((error) => {
    if (error) {
      console.error('Erreur Eureka :', error.message || error);
    } else {
      console.log('Enregistré dans Eureka sous le nom DONATION-SERVICE');
    }
  });

  const stop = () => client.stop(() => process.exit(0));
  process.on('SIGINT', stop);
  process.on('SIGTERM', stop);

  return client;
}

module.exports = { registerWithEureka };