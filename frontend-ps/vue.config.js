module.exports = {  
    outputDir: "../backend-ps/src/main/resources/static",  
    indexPath: "../static/index.html",  
    devServer: {  
      proxy: "http://localhost:8080"
    }

};