function Container(props) {
    console.log(props)
    return (
        <div className={"container"} style={props.styles}>
            {props.children}
        </div>
    )
}
export default Container;