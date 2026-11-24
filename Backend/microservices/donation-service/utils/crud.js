const express = require('express');

/**
 * Génère un CRUD REST complet pour un modèle Mongoose.
 * GET /          -> liste (pagination + filtres via query)
 * GET /:id       -> détail
 * POST /         -> création
 * PUT /:id       -> mise à jour
 * DELETE /:id    -> suppression
 */
function crudRouter(Model, { populate = '', filterFields = [] } = {}) {
  const router = express.Router();

  router.get('/', async (req, res, next) => {
    try {
      const page = Math.max(parseInt(req.query.page) || 1, 1);
      const limit = Math.min(parseInt(req.query.limit) || 20, 100);
      const filter = {};
      filterFields.forEach((f) => {
        if (req.query[f] !== undefined) filter[f] = req.query[f];
      });
      const [items, total] = await Promise.all([
        Model.find(filter)
          .populate(populate)
          .sort({ createdAt: -1 })
          .skip((page - 1) * limit)
          .limit(limit),
        Model.countDocuments(filter),
      ]);
      res.json({ items, page, limit, total });
    } catch (e) { next(e); }
  });

  router.get('/:id', async (req, res, next) => {
    try {
      const doc = await Model.findById(req.params.id).populate(populate);
      if (!doc) return res.status(404).json({ message: `${Model.modelName} introuvable` });
      res.json(doc);
    } catch (e) { next(e); }
  });

  router.post('/', async (req, res, next) => {
    try {
      const doc = await Model.create(req.body);
      res.status(201).json(doc);
    } catch (e) { next(e); }
  });

  router.put('/:id', async (req, res, next) => {
    try {
      const doc = await Model.findByIdAndUpdate(req.params.id, req.body, {
        new: true,
        runValidators: true,
      });
      if (!doc) return res.status(404).json({ message: `${Model.modelName} introuvable` });
      res.json(doc);
    } catch (e) { next(e); }
  });

  router.delete('/:id', async (req, res, next) => {
    try {
      const doc = await Model.findByIdAndDelete(req.params.id);
      if (!doc) return res.status(404).json({ message: `${Model.modelName} introuvable` });
      res.json({ message: `${Model.modelName} supprimé`, id: doc._id });
    } catch (e) { next(e); }
  });

  return router;
}

module.exports = crudRouter;
