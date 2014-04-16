/**
 * 
 */

Ext.define('Zero.store.base.ApplicationsConfigs', {
	extend : 'Zero.ex.store.ZStore',
	model:'Zero.model.base.ApplicationConfig',
	constructor : function () {
		var me = this;
		me.callParent(arguments);
		Ext.apply(me.proxy,{
			api : {
				read : '/console2/application/config/list.json',
				update : '/console2/application/config/modify.json',
				create : '/console2/application/config/add.json',
				destroy : '/console2/application/config/delete.json'
			}
		});
		
	}
});