/**
 * 
 */

Ext.define('Zero.store.base.ConfigProperties', {
	extend : 'Zero.ex.store.ZStore',
	model:'Zero.model.base.ConfigProperties',
	constructor : function () {
		var me = this;
		me.callParent(arguments);
		Ext.apply(me.proxy,{
			api : {
				read : '/console2/application/config/prop/list.json',
				update : '/console2/application/config/prop/modify.json',
				create : '/console2/application/config/prop/add.json',
				destroy : '/console2/application/config/prop/delete.json'
			}
		});
		
	}
});