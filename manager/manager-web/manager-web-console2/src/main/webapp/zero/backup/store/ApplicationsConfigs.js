/**
 * 
 */

Ext.define('Zero.store.base.ApplicationsConfigs', {
	extend : 'Ext.data.Store',
	storeId : 'base_servers',
	pageSize : 10,
	model:'Zero.model.base.ApplicationConfig',
});