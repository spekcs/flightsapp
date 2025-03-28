import LoginForm from "@/components/ui/loginform";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { useState } from "react";
import { Link } from "react-router-dom";

import {
  Card,
  CardContent,
  CardHeader,
  CardTitle,
  CardFooter
} from "@/components/ui/card"

function Register() {
    const navigate = useNavigate();
    const [errorMessage, setErrorMessage] = useState('')

    async function handleRegister(values: { username: string; password: string}) {
        axios.post("/api/user/register", values)
        .then(response => {
            localStorage.setItem("jwt", response.data.token);
            navigate("/")
        }).catch(error => {
            console.error("Error fetching data", error);
            setErrorMessage(error.response.data.message || error.message)
        })

    }

    return(<div className="h-screen flex justify-center items-center align-middle">
        <Card className="w-[350]px">
            <CardHeader>
                <CardTitle>Sign up</CardTitle>
            </CardHeader>
            <CardContent>
                <LoginForm onSubmit={handleRegister} />
                {errorMessage && (<p className="text-red-600 break-words whitespace-normal text-justify pt-6">{errorMessage}</p>
            )}
            </CardContent>
            <CardFooter className="flex justify-center">
                <Link to="/login" className="underline ">Sign in</Link>
            </CardFooter>
        </Card>
        </div>
    )
}

export default Register;