package com.ssubedir.ipcamera;

import android.util.Log;

import com.google.common.io.ByteStreams;
import com.google.protobuf.ByteString;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;
import io.grpc.stub.StreamObserver;

import static com.ssubedir.ipcamera.IpCameraGrpc.newStub;
import static io.grpc.okhttp.internal.Platform.logger;

public class Client {
    private ManagedChannel channel;
    private IpCameraGrpc.IpCameraBlockingStub ipCameraBlockingStub;
    private IpCameraGrpc.IpCameraStub asyncStub;
    public Client(String host, int port){
        this.channel = ManagedChannelBuilder.forAddress(host,port).usePlaintext().build();
        this.ipCameraBlockingStub = IpCameraGrpc.newBlockingStub(channel);
        this.asyncStub = IpCameraGrpc.newStub(channel);
    }

    public void Shutdown() throws InterruptedException {
        channel.shutdown().awaitTermination(5, TimeUnit.SECONDS);
    }

    public String Stream(String data){

        StreamRequest req = StreamRequest.newBuilder().setData(data).build();
        StreamResponse response;
        try {
            response = ipCameraBlockingStub.stream(req);
        } catch (StatusRuntimeException e) {
            return e.toString();
        }
        return response.getResponse();
    }

    public String StreamImage(ByteString data){
        StreamImageRequest req = StreamImageRequest.newBuilder().setImage(data).build();
        StreamImageResponse response;
        try {
            response = ipCameraBlockingStub.streamImage(req);
        } catch (StatusRuntimeException e) {
            return e.toString();
        }
        return response.getResponse();
    }


}
