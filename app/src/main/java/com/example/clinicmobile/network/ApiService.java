package com.example.clinicmobile.network;

import com.example.clinicmobile.dto.request.AppointmentRequest;
import com.example.clinicmobile.dto.request.ChangePasswordRequest;
import com.example.clinicmobile.dto.request.ForgotPasswordRequest;
import com.example.clinicmobile.dto.request.LoginRequest;
import com.example.clinicmobile.dto.request.PatientCreateRequest;
import com.example.clinicmobile.dto.request.RefreshTokenRequest;
import com.example.clinicmobile.dto.request.RegisterRequest;
import com.example.clinicmobile.dto.request.UpdateProfileRequest;
import com.example.clinicmobile.dto.response.ApiResponse;
import com.example.clinicmobile.dto.response.AppointmentResponse;
import com.example.clinicmobile.dto.response.AuthResponse;
import com.example.clinicmobile.dto.response.DoctorResponse;
import com.example.clinicmobile.dto.response.PageResponse;
import com.example.clinicmobile.dto.response.PatientResponse;
import com.example.clinicmobile.dto.response.SpecialtyResponse;
import com.example.clinicmobile.dto.response.TimeSlotResponse;
import com.example.clinicmobile.dto.response.UserProfileResponse;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @POST("auth/login")
    Call<ApiResponse<AuthResponse>> login(
            @Body LoginRequest request);

    @POST("auth/register")
    Call<ApiResponse<AuthResponse>> register(
            @Body RegisterRequest request);

    @POST("auth/forgot-password")
    Call<ApiResponse<Void>> forgotPassword(
            @Body ForgotPasswordRequest request);

    @POST("auth/change-password")
    Call<ApiResponse<Void>> changePassword(
            @Body ChangePasswordRequest request);

    @POST("auth/refresh")
    Call<AuthResponse> refreshToken(
            @Body RefreshTokenRequest request);

    @POST("auth/logout")
    Call<ApiResponse<Void>> logout(
            @Body RefreshTokenRequest request);

    @GET("patients/me")
    Call<ApiResponse<PatientResponse>> getMyProfile();

    @POST("patients")
    Call<ApiResponse<PatientResponse>> createProfile(
            @Query("userId") Long userId,
            @Body PatientCreateRequest request);

    @PUT("patients/{id}")
    Call<ApiResponse<PatientResponse>> updateProfile(
            @Path("id") Long patientId,
            @Body PatientCreateRequest request);

    @GET("specialties")
    Call<ApiResponse<List<SpecialtyResponse>>> getSpecialties();

    @GET("doctors")
    Call<ApiResponse<List<DoctorResponse>>> getDoctorsBySpecialty(
            @Query("specialtyId") Long specialtyId);

    @GET("doctors/{id}")
    Call<ApiResponse<DoctorResponse>> getDoctorById(
            @Path("id") Long id);

    @GET("timeslots")
    Call<ApiResponse<List<TimeSlotResponse>>> getTimeSlots(
            @Query("doctorId") Long doctorId,
            @Query("date") String date);

    @POST("appointments")
    Call<ApiResponse<AppointmentResponse>> createAppointment(
            @Body AppointmentRequest request);

    @GET("appointments/patient/{patientId}")
    Call<ApiResponse<PageResponse<AppointmentResponse>>> getMyAppointments(
            @Path("patientId") Long patientId,
            @Query("page") int page,
            @Query("size") int size);
}
