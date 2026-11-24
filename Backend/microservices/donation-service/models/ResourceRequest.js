const mongoose = require('mongoose');

const itemSchema = new mongoose.Schema({
  resourceId: { type: mongoose.Schema.Types.ObjectId, ref: 'Resource', required: true },
  quantity: { type: Number, required: true, min: 1 },
}, { _id: false });

const schema = new mongoose.Schema({
  requesterType: { type: String, enum: ['SHELTER', 'RESCUE_TEAM'], required: true },
  requesterId: { type: String, required: true },   // shelterId ou id équipe (externe)
  rescueMissionId: { type: String },               // externe (Animal Rescue)
  items: { type: [itemSchema], validate: [(v) => v.length > 0, 'Au moins une ressource est requise'] },
  priority: { type: String, enum: ['LOW', 'NORMAL', 'HIGH', 'URGENT'], default: 'NORMAL' },
  note: { type: String, trim: true },
  status: { type: String, enum: ['PENDING', 'APPROVED', 'REJECTED', 'FULFILLED'], default: 'PENDING' },
  decision: {
    decidedBy: { type: String },
    decidedAt: { type: Date },
    reason: { type: String },
  },
}, { timestamps: true });

module.exports = mongoose.model('ResourceRequest', schema);
