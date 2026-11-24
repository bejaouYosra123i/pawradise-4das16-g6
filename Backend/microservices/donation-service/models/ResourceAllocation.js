const mongoose = require('mongoose');

const itemSchema = new mongoose.Schema({
  resourceId: { type: mongoose.Schema.Types.ObjectId, ref: 'Resource', required: true },
  quantity: { type: Number, required: true, min: 1 },
}, { _id: false });

const schema = new mongoose.Schema({
  requestId: { type: mongoose.Schema.Types.ObjectId, ref: 'ResourceRequest', required: true },
  items: { type: [itemSchema], validate: [(v) => v.length > 0, 'Au moins une ressource est requise'] },
  allocatedBy: { type: String },
  marketplaceOrderId: { type: String }, // renseigné si achat via Marketplace
  status: { type: String, enum: ['ALLOCATED', 'CANCELLED'], default: 'ALLOCATED' },
  allocatedAt: { type: Date, default: Date.now },
}, { timestamps: true });

module.exports = mongoose.model('ResourceAllocation', schema);
