import LoginForm from "@/components/ui/loginform";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useState } from "react";

import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"


function Login() {
    const navigate = useNavigate();

    const [errorMessage, setErrorMessage] = useState('')

    async function handleLogin(values: { username: string; password: string}) {
        axios.post("/api/user/login", values)
        .then(response => {
            localStorage.setItem("jwt", response.data.token);
            navigate("/")
        }).catch(error => {
            console.error("Error fetching data", error);
            setErrorMessage(error.response.data.message)
        })

    }

    return(<div className="h-screen flex justify-center items-center align-middle">
        <Card className="w-[350]px max-w-sm">
            <CardHeader>
                <CardTitle>Login</CardTitle>
            </CardHeader>
            <CardContent>
                <LoginForm onSubmit={handleLogin} />
                {errorMessage && (<p className="text-red-600 break-words whitespace-normal text-justify pt-6">{errorMessage}</p>
            )}
            </CardContent>
        </Card>
        </div>
    )
}

export default Login;