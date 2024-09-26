(ns clj-vertx
  (:import [io.vertx.core Vertx]
           [io.vertx.ext.web Router]
           [io.vertx.core.json JsonObject]))

; import io.vertx.core.AbstractVerticle;
;
; public class Server extends AbstractVerticle {
;   public void start() {
;     vertx.createHttpServer().requestHandler(req -> {
;       req.response()
;         .putHeader("content-type", "text/plain")
;         .end("Hello from Vert.x!");
;     }).listen(8080);
;   }
; }
; // Source: https://vertx.io/

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn start-server! [_]
  (-> (Vertx/vertx)
      (.createHttpServer)
      (.requestHandler
       (fn [req]
         (-> (doto (.response req)
               (.putHeader "content-type" "text/plain")
               (.end "Hello from Vert.x on Clojure!")))))
      (.listen 8080)))

; package com.example.starter;
;
; import io.vertx.core.AbstractVerticle;
; import io.vertx.core.MultiMap;
; import io.vertx.core.Promise;
; import io.vertx.core.json.JsonObject;
; import io.vertx.ext.web.Router;
;
; public class MainVerticle extends AbstractVerticle {
;
;   @Override
;   public void start(Promise<Void> startPromise) throws Exception {
;     // Create a Router
;     Router router = Router.router(vertx);
;
;     // Mount the handler for all incoming requests at every path and HTTP method
;     router.route().handler(context -> {
;       // Get the address of the request
;       String address = context.request().connection().remoteAddress().toString();
;       // Get the query parameter "name"
;       MultiMap queryParams = context.queryParams();
;       String name = queryParams.contains("name") ? queryParams.get("name") : "unknown";
;       // Write a json response
;       context.json(
;         new JsonObject()
;           .put("name", name)
;           .put("address", address)
;           .put("message", "Hello " + name + " connected from " + address)
;       );
;     });
;
;     // Create the HTTP server
;     vertx.createHttpServer()
;       // Handle every request using the router
;       .requestHandler(router)
;       // Start listening
;       .listen(8888)
;       // Print the port on success
;       .onSuccess(server -> {
;         System.out.println("HTTP server started on port " + server.actualPort());
;         startPromise.complete();
;       })
;       // Print the problem on failure
;       .onFailure(throwable -> {
;         throwable.printStackTrace();
;         startPromise.fail(throwable);
;       });
;   }
; }
; // Source: https://vertx.io/get-started/

#_{:clj-kondo/ignore [:clojure-lsp/unused-public-var]}
(defn start-route-server! [_]
  (let [vertx (Vertx/vertx)
        router (Router/router vertx)]
    (-> (.route router)
        ; (.path "/some/path")
        (.handler (fn [context]
                    (let [address (-> (.request context)
                                      (.connection)
                                      (.remoteAddress)
                                      (.toString))
                          query-params (.queryParams context)
                          name-arg (if (.contains query-params "name")
                                     (.get query-params "name")
                                     "unknown")
                          json-object (doto (new JsonObject)
                                        (.put "name" name-arg)
                                        (.put "address" address)
                                        (.put "message" (str "Hello " name-arg " connected from " address)))]
                      (.json context json-object)))))
    (-> vertx
        (.createHttpServer)
        (.requestHandler router)
        (.listen 8888)
        (.onSuccess
         (fn [server]
           (prn "HTTP server started on port " (.actualPort server))))
        (.onFailure
         (fn [error]
           (prn "Error " error))))))
