// Description OpenAPI 3 du donation-service, pour le portail Swagger unifié de la Gateway.
// Reprend exactement les routes de routes/index.js (CRUD générique de utils/crud.js).

const resources = [
    // --- Donations ---
    { path: '/api/donations/campaigns',    tag: 'Fundraising Campaigns', filters: ['status', 'shelterId'] },
    { path: '/api/donations/donors',       tag: 'Donors',                filters: ['userId', 'email'] },
    { path: '/api/donations/sponsorships', tag: 'Sponsorships',          filters: ['donorId', 'targetType', 'status', 'animalId', 'shelterId'] },
    { path: '/api/donations/receipts',     tag: 'Donation Receipts',     filters: ['donationId'] },
    { path: '/api/donations',              tag: 'Donations',             filters: ['donorId', 'campaignId', 'paymentStatus', 'frequency'] },
    // --- Resources ---
    { path: '/api/resources/categories',    tag: 'Resource Categories',  filters: [] },
    { path: '/api/resources/inventory',     tag: 'Resource Inventory',   filters: ['resourceId'] },
    { path: '/api/resources/requests',      tag: 'Resource Requests',    filters: ['status', 'requesterId', 'requesterType', 'priority', 'rescueMissionId'] },
    { path: '/api/resources/allocations',   tag: 'Resource Allocations', filters: ['requestId', 'status'] },
    { path: '/api/resources/distributions', tag: 'Distributions',        filters: ['allocationId', 'status'] },
    { path: '/api/resources',               tag: 'Resources',            filters: ['categoryId', 'active'] },
];

function crudPaths({ path, tag, filters }) {
    const ok = (d) => ({ description: d });
    const body = { required: true, content: { 'application/json': { schema: { type: 'object' } } } };
    const id = [{ name: 'id', in: 'path', required: true, schema: { type: 'string' }, description: 'ObjectId MongoDB' }];
    const query = filters.map((f) => ({ name: f, in: 'query', required: false, schema: { type: 'string' } }));
    return {
        [path]: {
            get:  { tags: [tag], summary: `Lister (${tag})`, parameters: query, responses: { 200: ok('OK') } },
            post: { tags: [tag], summary: `Créer (${tag})`, requestBody: body, responses: { 201: ok('Créé'), 400: ok('Données invalides') } },
        },
        [`${path}/{id}`]: {
            get:    { tags: [tag], summary: `Récupérer par id (${tag})`, parameters: id, responses: { 200: ok('OK'), 404: ok('Introuvable') } },
            put:    { tags: [tag], summary: `Modifier (${tag})`, parameters: id, requestBody: body, responses: { 200: ok('OK'), 404: ok('Introuvable') } },
            delete: { tags: [tag], summary: `Supprimer (${tag})`, parameters: id, responses: { 204: ok('Supprimé'), 404: ok('Introuvable') } },
        },
    };
}

module.exports = function registerDocs(app) {
    app.get('/v3/api-docs', (req, res) => {
        res.json({
            openapi: '3.0.0',
            info: {
                title: 'Donation & Resource Management Service',
                version: '1.0.0',
                description: 'Microservice partagé (Node.js + Express + MongoDB)',
            },
            // les appels "Try it out" passent par la Gateway
            servers: [{ url: 'http://localhost:8888/donation' }],
            paths: Object.assign(
                { '/health': { get: { tags: ['Monitoring'], summary: 'État du service', responses: { 200: { description: 'UP' } } } } },
                ...resources.map(crudPaths)
            ),
        });
    });
};