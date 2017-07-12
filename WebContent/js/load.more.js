//默认加载部分和点击加载更多的商品，单独放
var result="da";
var transition=2;
$.ajax({
	type:"get",
	url:"SelAndDelServlet",
	success:function(data){//success或者complete都行，所有的逻辑就都在这里面 了,( ▼-▼ )。。。			
		//默认会先加载5个。然后下面一段启动代码就不会再执行了。
		var data0=data;
		data0=JSON.parse(data0);
		for(var i=0;i<5;i++){				
			var createdDiv=document.createElement("tr");//data0.good[i].good_picture								
			createdDiv.innerHTML="<td>"+data0.good[i].good_id+"</td><td>"+data0.good[i].good_name
								+"</td><td>"+data0.good[i].good_description+"</td><td>"+data0.good[i].good_price
								+"<span style='color:red;'><b>&yen;</b></span></td><td><img style='width:50px;height:50px;' src='"
								+data0.good[i].good_main_pic_path+"'></td><td><a onclick=updateFun('"+data0.good[i].good_id+"','"+data0.good[i].good_name+"','"+data0.good[i].good_description+"','"+data0.good[i].good_price+"')>修改</a></td><td><a onclick=deleteFun("+data0.good[i].good_id+")>删除</a></td>";											
																																																																			
			document.getElementById("tb").appendChild(createdDiv);//创建tb的子节点，也可以用body，但是会在最底部，父节点必须是有字节点的					
		}
		//点击按钮时才会触发下面的方法。
		$(document).ready(function(){//获取外面的，而外面无法获取不到这里里面的。				
			$(".loadMore").click(function(){					
				 result=data;//传过来的是json对象object。此时如果 alert(result);则弹出的是object这个单词。
					result=JSON.parse(result);//把传来的json对象(json外面无引号)转换为javascript对象，然后进行操作。													
					transition=transition+3;
					for(var i=transition;i<transition+3;i++){//json的index从5到15共11个。transition从5开始。因为在页面加载时已经加载了5个了。							
						var createdDiv=document.createElement("tr");
						createdDiv.innerHTML="<td>"+result.good[i].good_id+"</td><td>"+result.good[i].good_name
											+"</td><td>"+result.good[i].good_description+"</td><td>"+result.good[i].good_price
											+"<span style='color:red;'><b>&yen;</b></span></td><td><img style='width:50px;height:50px;' src='"
											+result.good[i].good_main_pic_path+"'></td><td><a onclick='updateFun('"+data0.good[i].good_id+"','"+result.good[i].good_name+"','"+result.good[i].good_description+"','"+result.good[i].good_price+"')'>修改</a></td><td><a onclick='deleteFun("+result.good[i].good_id+")'>删除</a></td>";
						document.getElementById("tb").appendChild(createdDiv);//创建tb的子节点，也可以用body，但是会在最底部，父节点必须是有子节点的								
					}
			})	//end $().click function										
	  })//end document.ready
	},//end success function
	error:function(){
		//do nothing
	}			
}) //end ajax. with of without ','
