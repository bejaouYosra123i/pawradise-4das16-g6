const crudRouter = require('../utils/crud');
const FundraisingCampaign = require('../models/FundraisingCampaign');
const Donor = require('../models/Donor');
const Donation = require('../models/Donation');
const Sponsorship = require('../models/Sponsorship');
const DonationReceipt = require('../models/DonationReceipt');
const ResourceCategory = require('../models/ResourceCategory');
const Resource = require('../models/Resource');
const ResourceInventory = require('../models/ResourceInventory');
const ResourceRequest = require('../models/ResourceRequest');
const ResourceAllocation = require('../models/ResourceAllocation');
const Distribution = require('../models/Distribution');

module.exports = function registerRoutes(app) {
  // --- Donations (les routes spécifiques AVANT /api/donations) ---
  app.use('/api/donations/campaigns', crudRouter(FundraisingCampaign, { filterFields: ['status', 'shelterId'] }));
  app.use('/api/donations/donors', crudRouter(Donor, { filterFields: ['userId', 'email'] }));
  app.use('/api/donations/sponsorships', crudRouter(Sponsorship, {
    populate: 'donorId', filterFields: ['donorId', 'targetType', 'status', 'animalId', 'shelterId'],
  }));
  app.use('/api/donations/receipts', crudRouter(DonationReceipt, { populate: 'donationId', filterFields: ['donationId'] }));
  app.use('/api/donations', crudRouter(Donation, {
    populate: 'donorId campaignId', filterFields: ['donorId', 'campaignId', 'paymentStatus', 'frequency'],
  }));

  // --- Resources (les routes spécifiques AVANT /api/resources) ---
  app.use('/api/resources/categories', crudRouter(ResourceCategory));
  app.use('/api/resources/inventory', crudRouter(ResourceInventory, { populate: 'resourceId', filterFields: ['resourceId'] }));
  app.use('/api/resources/requests', crudRouter(ResourceRequest, {
    filterFields: ['status', 'requesterId', 'requesterType', 'priority', 'rescueMissionId'],
  }));
  app.use('/api/resources/allocations', crudRouter(ResourceAllocation, { filterFields: ['requestId', 'status'] }));
  app.use('/api/resources/distributions', crudRouter(Distribution, { filterFields: ['allocationId', 'status'] }));
  app.use('/api/resources', crudRouter(Resource, { populate: 'categoryId', filterFields: ['categoryId', 'active'] }));
};
