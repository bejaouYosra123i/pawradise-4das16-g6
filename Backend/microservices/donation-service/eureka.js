const { Eureka } = require('eureka-js-client');

function registerWithEureka(port) {
  const urls = (process.env.EUREKA_URL || 'http://localhost:8761/eureka/')
    .split(',')
    .map((u) => u.trim())
    .filter(Boolean);
  const host = process.env.HOST_IP || 'localhost';
  const appName = 'donation-service';

  const buildInstance = () => ({
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
  });

  // Un client par serveur Eureka : le service s'enregistre sur chacun
  const clients = urls.map((u) => {
    const eurekaUrl = new URL(u);
    const client = new Eureka({
      instance: buildInstance(),
      eureka: {
        host: eurekaUrl.hostname,
        port: Number(eurekaUrl.port) || 8761,
        servicePath: eurekaUrl.pathname.replace(/\/$/, '') + '/apps/',
      },
    });

    client.start((error) => {
      if (error) {
        console.error(`Erreur Eureka (${eurekaUrl.host}) :`, error.message || error);
      } else {
        console.log(`Enregistré dans Eureka ${eurekaUrl.host} sous le nom DONATION-SERVICE`);
      }
    });
    return client;
  });

  const stop = () =>
    Promise.all(clients.map((c) => new Promise((resolve) => c.stop(resolve))))
      .then(() => process.exit(0));
  process.on('SIGINT', stop);
  process.on('SIGTERM', stop);

  return clients;
}

module.exports = { registerWithEureka };