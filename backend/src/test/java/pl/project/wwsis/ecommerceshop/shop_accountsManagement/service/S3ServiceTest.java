package pl.project.wwsis.ecommerceshop.shop_accountsManagement.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class S3ServiceTest {

    private S3Client s3Client;
    private S3Service s3Service;

    @BeforeEach
    void setUp() {
        s3Client = Mockito.mock(S3Client.class);
        s3Service = new S3Service(s3Client);
    }

    @Test
    void putObject() throws IOException {
        //given
        String customer = "customer";
        String key = "key";
        byte[] bytes = "Hello world!".getBytes();

        //when
        s3Service.putObject(customer, key, bytes);

        //then
        ArgumentCaptor<PutObjectRequest> requestArgumentCaptor = ArgumentCaptor.forClass(PutObjectRequest.class);
        ArgumentCaptor<RequestBody> requestBodyArgumentCaptor = ArgumentCaptor.forClass(RequestBody.class);

        Mockito.verify(s3Client).putObject(requestArgumentCaptor.capture(), requestBodyArgumentCaptor.capture());

        assertThat(requestArgumentCaptor.getValue().bucket()).isEqualTo(customer);
        assertThat(requestArgumentCaptor.getValue().key()).isEqualTo(key);
        assertThat(requestBodyArgumentCaptor.getValue().contentStreamProvider().newStream().readAllBytes()).isEqualTo(bytes);
    }

    @Test
    void getObject() throws IOException {
        //given
        String customer = "customer";
        String key = "key";
        byte[] bytes = "Hello world!".getBytes();
        GetObjectRequest objectRequest = GetObjectRequest.builder().key(key).bucket(customer).build();

        ResponseInputStream inputStreamMock = Mockito.mock(ResponseInputStream.class);
        when(inputStreamMock.readAllBytes()).thenReturn(bytes);

        when(s3Client.getObject(objectRequest)).thenReturn(inputStreamMock);

        //when
        byte[] bytesActual = s3Service.getObject(customer, key);

        //then
        assertThat(bytesActual).isEqualTo(bytes);
    }

    @Test
    void getObjectWillThrowWhenReadAllBytes() throws IOException {
        //given
        String customer = "customer";
        String key = "key";
        byte[] bytes = "Hello world!".getBytes();
        GetObjectRequest getObjectRequest = GetObjectRequest.builder().key(key).bucket(customer).build();

        ResponseInputStream inputStreamMock = Mockito.mock(ResponseInputStream.class);
        when(inputStreamMock.readAllBytes()).thenThrow(new IOException("can not read it"));
        when(s3Client.getObject(eq(getObjectRequest))).thenReturn(inputStreamMock);

        //when
        //then
        assertThatThrownBy(() -> s3Service.getObject(customer, key))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("can not read it")
                .hasRootCauseInstanceOf(IOException.class);
    }
}