/**
 * 
 */
Ext.define('Zero.view.AbstractZeroView', {
	extend:'Ext.panel.Panel',
	initComponent:function () {
		var me = this;
		if(!this['controllers']) {
			Ext.Error.raise('Controller UnDefine!!!');
		}
		else if(Ext.isArray(this['controllers']) && this['controllers'].length < 1 ) {
			Ext.Error.raise('Controller UnDefine!!!');
		} 
		else if(this['controllers'] == ''){
			Ext.Error.raise('Controller UnDefine!!!');
		}
		
		this.callParent();
		console.log("Init AbstractZeroView");
		this.on('afterrender', this.registControllers, this);
	},
	registControllers : function() {
		//Instanciate the controllers into the global Applications controllers array
		Ext.each(this.controllers, function(control) {
			console.log("Cached Controllers ",Zero.app.controllers);
			var controller = Zero.app.controllers.get(control);
			if (!controller) {
				console.log("Create Controller",control);
				controller = Ext.create(control, {
					application : Zero.app,
					id : control
				});
				console.log("Create Finish Controller");
				Zero.app.controllers.put(control, controller);
				controller.init(); // Run init on the controller
				console.log("Register Controller");
			}
			//controller.init(); //Run init on the controller         
		});
	}
});