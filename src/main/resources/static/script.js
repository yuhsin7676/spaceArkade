class Graphic{
    
    canvas = document.getElementById("canvas");
    ctx = canvas.getContext('2d');
    turnOver = false;
    
    update(data){
        this.clear();
        for(var key in data.worldPointer.components){
            var obj = data.worldPointer.components[key]; 
            var locationX, locationY;
            
            if(this.turnOver){
                locationX = data.worldPointer.width - obj.location.x;
                locationY = data.worldPointer.height - obj.location.y;
            }
            else{
                locationX = obj.location.x;
                locationY = obj.location.y;
            }
            
            if(obj.isCircle){
                this.ctx.beginPath();
                this.ctx.arc(locationX, locationY, obj.r, 0, Math.PI*2, true);
                this.ctx.stroke();
            }
            else{
                this.ctx.fillRect(locationX - obj.size.x/2, locationY - obj.size.y/2, obj.size.x, obj.size.y);
            }
        }
    }
    
    clear(){
        this.ctx.clearRect(0, 0, this.canvas.width, this.canvas.height);
    }
    
}

class Inputs{
    
    _toLeft = 0;
    _toRight = 0;
    velocityX = 0;
    turnOver = false;
    
    constructor(){
        document.addEventListener('keydown', (event) => {
            if(event.code === "KeyA")
                this._toLeft = -1;
            else if(event.code === "KeyD")
                this._toRight = 1;
        });

        document.addEventListener('keyup', (event) => {
            if(event.code === "KeyA")
                this._toLeft = 0;
            else if(event.code === "KeyD")
                this._toRight = 0;
        });
    }
    
    update(){
        if(this.turnOver)
            this.velocityX = -400 * (this._toLeft + this._toRight);
        else
            this.velocityX = 400 * (this._toLeft + this._toRight);
    }
    
}

var begin = true;
var inputs = new Inputs();
var graphic = new Graphic();

//////////////// Работа с сокетом //////////////////

var socket = new WebSocket("ws://localhost:8888/websocket");

socket.onmessage = function(event) {
    if(event.data !== "wait" && event.data !== "noPlay"){       
        
        begin = false;
        document.getElementById("messages").innerHTML = "";
        
        var data = JSON.parse(event.data);
        if(data.playerNumber == 1)
            graphic.turnOver = inputs.turnOver = true;
        
        graphic.update(data);
        inputs.update();
        socket.send(JSON.stringify({x: inputs.velocityX, y: 0}));
        
    }
    else if(event.data === "noPlay"){
        
        graphic.clear();
        if(!begin)
            document.getElementById("messages").innerHTML = "Вы проиграли!";
        
    }
    else if(event.data === "wait"){
        document.getElementById("messages").innerHTML = "Ждем второго игрока!";
    }
};

socket.onclose = function(event) {
    console.log("Соединение сброшено!");
};

socket.onerror = function(error) {
    console.log(`[error] ${error.message}`);
};

