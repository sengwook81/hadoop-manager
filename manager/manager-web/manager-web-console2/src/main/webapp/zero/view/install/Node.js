/**
 * 
 */
// 노드 목록 조회 뷰 정의.
Ext.define('Zero.view.install.Node',{
	extend:'Ext.panel.Panel',
	layout:{
		type:'vbox',
		align:'stretch'
	},
	alias:'widget.node',
	id:'installnode',
	controllers:['Zero.controller.install.NodeCTR'],
	requires: [
	    'Zero.store.install.Nodes'
    ],
    initComponent:function () {
    	me = this;
    	Ext.apply(me, {
    		items:[
    		   me.masterGrid()
    		]
    	});
    	me.callParent(arguments);
    	me.on('afterrender', me.registControllers, me);
    },
	masterGrid:function () {
		return Ext.create('Ext.grid.Panel',{
			id:'nodegrid',
			store:Ext.data.StoreManager.lookup('install_nodes'),
			height:300,
    		columns:[
    		{
    			text:'ID',dataIndex:'node_id',width:30
    		}, {
    			text:'HOST',dataIndex:'node_addr',width:80
    		}, {
    			text:'NAME',dataIndex:'node_name',width:80
    		}, {
    			text:'USER',dataIndex:'node_user',width:80
    		}, {
    			text:'PASSWORD',dataIndex:'node_password',renderer:function () {
    				return "*****";
    			}
    		}, {
    			text:'HOME',dataIndex:'node_home'
    		}
	        ],
		});
	},formView:function () {
		return Ext.create('Ext.form.FormPanel',{
			flex:1
		});
	},
	registControllers: function(){
	        //Instanciate the controllers into the global Applications controllers array
	     Ext.each(this.controllers, function(control){
	            var controller = Zero.app.controllers.get(control);
	            if (!controller) {
	                controller = Ext.create(control, {
	                    application: Zero.app,
	                    id: control
	                });
	                Zero.app.controllers.put(control, controller);
					controller.init(); // Run init on the controller
	            }
	       		//controller.init(); //Run init on the controller         
	        });
	    }
	
});