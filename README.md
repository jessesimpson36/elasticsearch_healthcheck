optimize deployment:
```
      - env:
        - name: ELASTICSEARCH_HOSTNAME
          value: "elasticsearch-master"
        - name: ELASTICSEARCH_PORT
          value: "9200"
        - name: HOSTNAME
          value: "0.0.0.0"
        ports:
        - containerPort: 8001
        name: elasticsearchhealthcheck
        image: jessesimpson/elasticsearch_healthcheck:0.0.4


          # put this under optimize deployment
        livenessProbe:
          exec:
            command:
            - wget
            - -O
            - "-"
            - http://127.0.0.1:8001/healthcheck
          failureThreshold: 2
          initialDelaySeconds: 10
          periodSeconds: 30
          successThreshold: 1
          timeoutSeconds: 1
```
