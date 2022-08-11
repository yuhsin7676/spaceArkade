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
                if(obj.className == "Ball") this.ctx.fillStyle = "#000";
                else if(obj.className == "Bonus") this.ctx.fillStyle = "#f00";
                
                this.ctx.arc(locationX, locationY, obj.r, 0, Math.PI*2, true);
                this.ctx.stroke();
            }
            else{
                if(obj.className == "Tile1") this.ctx.fillStyle = "#080";
                else if(obj.className == "Tile2") this.ctx.fillStyle = "#cc0";
                else if(obj.className == "Tile3") this.ctx.fillStyle = "#f80";
                else if(obj.className == "Platform") this.ctx.fillStyle = "#000";
                
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

var inputs = new Inputs();
var graphic = new Graphic();
var messages = document.getElementById("messages");

//////////////// Работа с сокетом //////////////////

// Сюда надо написать ip-адрес
var socket = new WebSocket("ws://localhost:8888/websocket");

socket.onmessage = function(event) {
    messages.innerHTML = "";
    
    switch(event.data){
        case "NOPLAY":
            graphic.clear();
            break;
        case "WAIT":
            graphic.clear();
            messages.innerHTML = "Ждем второго игрока!";
            break;
        case "LOSE":
            graphic.clear();
            messages.innerHTML = "Вы проиграли!";
            break;
        case "WIN":
            graphic.clear();
            messages.innerHTML = "Вы выиграли!";
            break;
        default:
            var data = JSON.parse(event.data);
            if(data.playerNumber == 1)
                graphic.turnOver = inputs.turnOver = true;
            else
                graphic.turnOver = inputs.turnOver = false;
            
            graphic.update(data);
            inputs.update();
            socket.send(JSON.stringify({x: inputs.velocityX, y: 0})); 
    }
};

socket.onclose = function(event) {
    console.log("[close] Соединение закрыто чисто, код: " + event.code + "; причина:" + event.reason);
};

socket.onerror = function(error) {
    console.error(error.message);
};